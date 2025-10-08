import datetime
import os

import requests
import uvicorn
from fastmcp import FastMCP
from fastmcp.server.auth.providers.google import GoogleProvider
from fastmcp.server.dependencies import get_access_token
from pydantic import BaseModel
from starlette.middleware import Middleware
from starlette.middleware.cors import CORSMiddleware

# Configure token validation for your identity provider
google = GoogleProvider(
  client_id=os.getenv("MCP_GOOGLE_CLIENT_ID"),
  client_secret=os.getenv("MCP_GOOGLE_CLIENT_SECRET"),
  base_url="http://localhost:8001",
  required_scopes=[
    "openid",
    "https://www.googleapis.com/auth/calendar.freebusy"
  ],
)
calendar_id = os.getenv("MCP_CALENDAR_ID")

# This for CORS support
custom_middleware = [
  Middleware(CORSMiddleware, allow_origins=["*"],
             allow_methods=["*"],
             allow_headers=["*"], ),
]

mcp = FastMCP("My MCP Server", auth=google)


class BusyPeriod(BaseModel):
  start: str
  end: str


class BusyResponse(BaseModel):
  busy_periods: list[BusyPeriod]


@mcp.tool(name="is-busy", description="Find out whether the time blocks where the user is busy, between two dates, inclusive")
def is_busy(start_date: datetime.date, end_date: datetime.date) -> BusyResponse:
  access_token_obj = get_access_token()
  access_token = access_token_obj.token

  # Convert dates to RFC3339 format
  time_min = datetime.datetime.combine(start_date, datetime.time.min).isoformat() + "Z"
  time_max = datetime.datetime.combine(end_date, datetime.time.max).isoformat() + "Z"

  # Make request to Google Calendar freebusy API
  url = "https://www.googleapis.com/calendar/v3/freeBusy"
  headers = {
    "Authorization": f"Bearer {access_token}",
    "Content-Type": "application/json"
  }
  body = {
    "timeMin": time_min,
    "timeMax": time_max,
    "items": [{"id": calendar_id}]
  }
  response = requests.post(url, headers=headers, json=body)
  response.raise_for_status()

  # Extract busy periods
  data = response.json()
  busy_periods = data.get("calendars", {}).get(calendar_id, {}).get("busy", [])

  return BusyResponse(
    busy_periods=[BusyPeriod(**period) for period in busy_periods]
  )


app = mcp.http_app(transport="http", path="/mcp", middleware=custom_middleware)

if __name__ == "__main__":
  uvicorn.run("google_calendar:app", port=8001)
