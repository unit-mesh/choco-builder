---
layout: default
title: FAQ
nav_order: 109
permalink: /faq
---

## response status: 413

需要配置 Nginx 的 `client_max_body_size` 参数，否则会出现 413 的错误。

示例：

```nginx
http {
    ...
    client_max_body_size 20m;
    ...
}
```

```bash
[SCANNER] o.a.s.ctl.client.ArchGuardHttpClient process topic: class-items
[SCANNER] o.a.s.ctl.client.ArchGuardHttpClient                 response status: 413
response body: <html>
<head><title>413 Request Entity Too Large</title></head>
<body>
<center><h1>413 Request Entity Too Large</h1></center>
<hr><center>nginx/1.22.1</center>
</body>
</html>
``` 