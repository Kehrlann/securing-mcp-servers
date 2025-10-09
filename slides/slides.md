---
theme: default
class: 'text-center'
highlighter: shiki
lineNumbers: true
transition: none
# use UnoCSS
css: unocss
aspectRatio: "16/9"
colorSchema: "light"
layout: cover
---

# **Securing MCP Servers**

<br>

### _AI's all fun & games, until your first data leak_

<br>
<br>

### Daniel Garnier-Moiroux

Devoxx Belgium, 2025-10-09

---
layout: image-right
image: /daniel-intro-small.jpg
class: smaller
---

#### Daniel
### Garnier-Moiroux
<br>

Software Engineer @ Broadcom

- <logos-spring-icon /> Spring
- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <logos-github-icon /> github.com/Kehrlann/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf

---

# Securing MCP Servers

1. **MCP: what and how?**
1. A brief history
1. All about OAuth2
1. In practice: Spring AI integration
1. Other implementations: Python's FastMCP
1. Notable alternative: API keys

---

# MCP: what and how?

&nbsp;

> MCP (Model Context Protocol) is an open-source standard for connecting AI applications to external systems.
>
> &nbsp;
>
> <i>https://modelcontextprotocol.io/docs/getting-started/intro</i>


---
layout: image
image: mcp-simple-diagram.png
class: background-contain
---

---
layout: image
image: mcp-architecture-clarification.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-1.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-2.png
class: background-contain
---

---
layout: image
image: mcp-stdio-vs-http-3.png
class: background-contain
---

---

# Why authorization?

&nbsp;

Ensure that:
- User is known to the system
- User can see only their resources
- User has permission to perform an action
- System is auditable
- ...

---

# Some AI apps we'll use

&nbsp;

- Claude Code
- MCP inspector
    - Not an AI app, only does the MCP client part!

But also Cursor, Windsurf, Claude Desktop, ...

---

# Securing MCP Servers

1. MCP: what and how?
1. **A brief history**
1. All about OAuth2
1. In practice: Spring AI integration
1. Other implementations: Python's FastMCP
1. Notable alternative: API keys


---
layout: image
image: mcp-history-1.png
---

---
layout: image
image: mcp-history-2.png
---

---
layout: image
image: mcp-history-3.png
---

---
layout: image
image: mcp-history-4.png
---

---
layout: image
image: mcp-history-5.png
---

---
layout: image
image: mcp-history-6.png
---

---

# The spec: use OAuth2

- **MCP Server** == OAuth2 Resource Server
    - => Authorizes with valid JWT tokens

- **MCP Client** == OAuth2 Client
    - => Requests tokens on behalf of user

- An **OAuth2 Authorization Server**
    - Validates user identity, issues tokens

---

# It's Complicated™

- OAuth 2.1 IETF DRAFT (draft-ietf-oauth-v2-1-13)
- OAuth 2.0 Authorization Server Metadata (RFC8414)
- OAuth 2.0 Dynamic Client Registration Protocol (RFC7591)
- OAuth 2.0 Protected Resource Metadata (RFC9728)
- Resource Indicators for OAuth 2.0 (RFC8707)
- JSON Web Token (JWT) Profile for OAuth 2.0 Access Tokens (RFC9068)


---
layout: image
image: /mcp-oauth-simple.png
class: background-contain
---

---
layout: image
image: /mcp-oauth-full.png
class: background-contain
---

---

# It's (very) Complicated™

&nbsp;

This is still very new:

- Noone truly implements the whole stack
- Interoperability needs improvement
- Too complex for developers, too many failure modes
- It _will_ change

---

# Securing MCP Servers

1. MCP: what and how?
1. A brief history
1. **All about OAuth2**
1. In practice: Spring AI integration
1. Other implementations: Python's FastMCP
1. Notable alternative: API keys

---

# OAuth 2 & 2.1

<br>

- An *Authorization* framework
  - Goal: Grant **applications** the **permission** to access **ressources** through **HTTP**.
- Using *tokens*, in this case `access_token`
- A long list of specs
  - **https://oauth.net/specs/** (sometimes a bit ... dry ...)

---

# OAuth 2 & 2.1

For example:

🧑🏻 **Daniel**

authorizes

🖥️ **my-photo-book.example.com**

to access his pictures hosted on

📸 **Google Photos**

(without sharing his 🔐 Google password)

---

# OAuth 2 & 2.1: for MCP clients

For example:

🧑🏻 **Daniel**

authorizes

🖥️ [**Claude Desktop** | ChatGPT | Cursor]

to [access resources | call tools | ...] on

📸 **Awesome Company MCP Server**

(without sharing his 🔐 Company password)

---
layout: fact
---

# How and why?

---
layout: image
image: /sso-1-give-password.png
class: background-contain
---

---
layout: image
image: /sso-2-give-password-bad-idea.png
class: background-contain
---

---
layout: image
image: /sso-3-high-level-authorization-code.png
class: background-contain
---

---
layout: image
image: /sso-5-more-details-oauth.png
class: background-contain
---

---
layout: image
image: /sso-for-mcp.png
class: background-contain
---

---

# It's all about trust

&nbsp;

There are many relationships between actors, with varying levels of trust.

The client <> server relation **MUST** be trusted.

---
layout: image
image: /sso-trust-relationship.png
class: background-contain
---

---
layout: image
image: /sso-mcp-no-trust.png
class: background-contain
---

---

# Client <> Auth Server trust

&nbsp;

Two options:

- The MCP Client is pre-registered with the Auth Server
    - Great for remote app (Spring AI, ...), with known MCP servers
- The MCP Client is _not_ pre-registered
    - Needs OAuth2 Dynamic Client Registration
    - Better UX for local apps

---

# Securing MCP Servers

1. MCP: what and how?
1. A brief history
1. All about OAuth2
1. **In practice: Spring AI integration**
1. Other implementations: Python's FastMCP
1. Notable alternative: API keys

---

# Spring AI integration

<br>

<logos-github-icon /> https://github.com/spring-ai-community/mcp-security/

Security for:
- Spring AI MCP Servers
- Spring AI MCP Clients
- Spring Authorization Server

---
layout: image
image: /mcp-oauth-simple.png
class: background-contain
---

---

# Securing MCP Servers

1. MCP: what and how?
1. A brief history
1. All about OAuth2
1. In practice: Spring AI integration
1. **Other implementations: Python's FastMCP**
1. Notable alternative: API keys

---

# Python's FastMCP

<br>

### https://gofastmcp.com


---
layout: image
image: /fastmcp-oauth-proxy.png
class: background-contain
---

---
layout: image
image: /fastmcp-token-passthrough.png
class: background-contain
---

---

# Securing MCP Servers

1. MCP: what and how?
1. A brief history
1. All about OAuth2
1. In practice: Spring AI integration
1. Other implementations: Python's FastMCP
1. **Notable alternative: API keys**

---

# Notable alternatives: API key

<br>

<logos-github-icon /> https://github.com/spring-ai-community/mcp-security/


---

## References

&nbsp;

#### <logos-github-icon /> https://github.com/Kehrlann/securing-mcp-servers

<!-- qrencode -s 9 -m 2 -o qr-code.png https://m.devoxx.com/events/dvbe25/talks/6226/securing-mcp-servers -->
<div style="float:right; margin-right: 50px; text-align: center;">
    <a href="https://m.devoxx.com/events/dvbe25/talks/6226/securing-mcp-servers" target="_blank">
        <img src="/qr-code-devoxx-be.png" style="margin-bottom: -45px; height: 300px;" >
    </a>
</div>

<br>

- <logos-bluesky /> @garnier.wf
- <logos-firefox /> https://garnier.wf/
- <fluent-emoji-flat-envelope-with-arrow /> contact@garnier.wf


---
layout: image
image: /meet-me.jpg
class: end
---

# **Merci 😊**

