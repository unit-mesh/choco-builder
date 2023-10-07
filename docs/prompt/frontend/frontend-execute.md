---
layout: default
title: UI 设计 - Execute 阶段
parent: Prompt Sample
nav_order: 3
---

你是一个资深的前端开发人员，帮助编写用户设计好的前端 UI。你需要根据下面的需求和页面，生成对应的前端代码。

- 项目的技术栈是 React + TypeScript + Material UI。

请根据用户提供的问题，生成前端代码。

相关的组件示例如下：
###
grid component:
 ```design
<Grid container spacing={3}>
  <Grid item xs>
    <Item>xs</Item>
  </Grid>
  <Grid item xs={6}>
    <Item>xs=6</Item>
  </Grid>
  <Grid item xs>
    <Item>xs</Item>
  </Grid>
</Grid>
```
box component:
 ```design
<Button>Save</Button>
```
avatars component:
 ```design
<Avatar {...stringAvatar('Kent Dodds')} />
<Avatar {...stringAvatar('Jed Watson')} />
<Avatar {...stringAvatar('Tim Neutkens')} />
```
typography component:
```design
<Div>{"This div's text looks like that of a button."}</Div>
```
pagination component:
 ```design
<Pagination count={10} />
<Pagination count={10} color="primary" />
<Pagination count={10} color="secondary" />
<Pagination count={10} disabled />
```
###

用户提供的问题：
编写一个响应式布局的博客发表页，需要包含标题、内容、时间、存为草稿，如果创建成功则弹出对话框，显示：“创建成功!”

页面布局要求：

```design
------------------------------------------------------
|      NavComponent(12x)                             |
------------------------------------------------------
|    Text(6x, "标题")              |   Empty(6x)      |
------------------------------------------------------
|Avatar(3x, "头像") | Date(3x, "发布时间") | Empty(6x) |
------------------------------------------------------
| CardMedia(8x)                   |   Empty(4x)      |
------------------------------------------------------
| Typography(12x, "内容")                             |
------------------------------------------------------
| FooterComponent(12x)                               |
------------------------------------------------------
```

现在请你生成前端代码，代码使用 Markdown 语言编写，以便用户可以直接复制到项目中。


## Output

```tsx
import React from 'react';
import { Grid, Avatar, Typography, Button, CardMedia } from '@material-ui/core';

const BlogPostPage: React.FC = () => {
  const handleSave = () => {
    // 保存草稿的逻辑
    alert('创建成功!');
  };

  return (
    <Grid container spacing={3}>
      {/* NavComponent */}
      <Grid item xs={12}>
        <NavComponent />
      </Grid>

      {/* 标题 */}
      <Grid item xs={6}>
        <Typography variant="h6">标题</Typography>
      </Grid>
      <Grid item xs={6}></Grid>

      {/* 头像 */}
      <Grid item xs={3}>
        <Avatar />
      </Grid>
      {/* 发布时间 */}
      <Grid item xs={3}>
        <Typography variant="body2">发布时间</Typography>
      </Grid>
      <Grid item xs={6}></Grid>

      {/* CardMedia */}
      <Grid item xs={8}>
        <CardMedia />
      </Grid>
      <Grid item xs={4}></Grid>

      {/* 内容 */}
      <Grid item xs={12}>
        <Typography variant="body1">内容</Typography>
      </Grid>

      {/* FooterComponent */}
      <Grid item xs={12}>
        <FooterComponent />
      </Grid>
    </Grid>
  );
};

export default BlogPostPage;
``` 
