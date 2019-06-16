To authenticate:
1. Use **Authorization Controller** for obtaining an auth token.
2. Click on the **Authorize** button at the top of the page.
3. Paste *'Bearer &lt;token&gt;'* as an API key value and hit **Authorize**.

After that, all of the requests to the portal via Swagger UI will be enriched with Authorization header holding *'Bearer &lt;token&gt;'* value.
