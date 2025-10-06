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

1. A brief history
    1. A timeline
    1. The decision: oauth2
    1. New stuff lol
1. All about OAuth2
    1. How oauth2 works
    1. In MCP land
    1. Spring AI integration
    1. No token passthrough 😱
1. Notable alternative: API keys
    1. Demo
    1. Drawbacks

---

# Securing MCP Servers

1. A brief history
1. All about OAuth2
1. In practice: Spring AI integration
1. Notable alternative: API keys

---

# Securing MCP Servers

1. **A brief history**
1. All about OAuth2
1. In practice: Spring AI integration
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
layout: image
image: mcp-history-7.png
---

---
layout: image
image: mcp-history-8.png
---

---
layout: image
image: mcp-history-9.png
---

---
layout: image
image: mcp-history-10.png
---

---
layout: image
image: mcp-history-11.png
---

---
layout: image
image: mcp-history-12.png
---

---
layout: image
image: mcp-history-13.png
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
layout: image
image: rfc-pop-quizz-1.png
---

---
layout: image
image: rfc-pop-quizz-2.png
---

---

# It's Complicated™

&nbsp;

Noone truly implements the full stack.

It's early! It _will_ change.

---

# Securing MCP Servers

1. A brief history
1. **All about OAuth2**
1. In practice: Spring AI integration
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

There's a lot of relationships between actors.

The client <> server relation MUST be trusted.

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

## References

&nbsp;

#### <logos-github-icon /> https://github.com/Kehrlann/spring-security-authorization

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

