import uvicorn
from fastmcp import FastMCP
from fastmcp.server.auth import RemoteAuthProvider
from fastmcp.server.auth.providers.jwt import JWTVerifier
from pydantic import AnyHttpUrl
from starlette.middleware import Middleware
from starlette.middleware.cors import CORSMiddleware
from fastmcp.server.dependencies import get_access_token, AccessToken

# Configure token validation for your identity provider
token_verifier = JWTVerifier(
    jwks_uri="http://localhost:9000/oauth2/jwks",
    issuer="http://localhost:9000",
    # Optional: restrict audience
    # audience="mcp-production-api"
)

# Create the remote auth provider
auth = RemoteAuthProvider(
    token_verifier=token_verifier,
    authorization_servers=[AnyHttpUrl("http://localhost:9000")],
    base_url="http://localhost:8000",  # Your server base URL
)

# This for CORS support
custom_middleware = [
    Middleware(CORSMiddleware, allow_origins=["*"],
               allow_methods=["*"],
               allow_headers=["*"],),
]

mcp = FastMCP("My MCP Server", auth=auth)

@mcp.tool
def greet(language: str) -> str:
    access_token = get_access_token()
    return f"Hello, {access_token.claims["name"]}!"

if __name__ == "__main__":
    mcp.run(transport="streamable-http",path="/mcp")