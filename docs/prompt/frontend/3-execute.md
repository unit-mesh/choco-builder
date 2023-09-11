---
layout: default
title: UI 设计：Execute 阶段
parent: Prompt logs
nav_order: 3
---

你是一个资深的前端开发人员，帮助编写用户设计好的前端 UI。你需要根据下面的需求和页面，生成对应的前端代码。
- 项目的技术栈是 React + TypeScript + Material UI。

请根据用户提供的问题，生成前端代码。

相关的组件列表如下：
grid
: ```ComponentExample(name=AutoGrid, content=<Grid container spacing={3}>
<Grid item xs>
<Item>xs</Item>
</Grid>
<Grid item xs={6}>
<Item>xs=6</Item>
</Grid>
<Grid item xs>
<Item>xs</Item>
</Grid>
</Grid>)```
box
: ```ComponentExample(name=BoxComponent, content=<Button>Save</Button>)```
typography
: ```ComponentExample(name=TypographyTheme, content=<Div>{"This div's text looks like that of a button."}</Div>)```
buttons
: ```ComponentExample(name=BasicButtons, content=<Button variant="text">Text</Button>
<Button variant="contained">Contained</Button>
<Button variant="outlined">Outlined</Button>)```
dialogs
: ```ComponentExample(name=SimpleDialogDemo, content=<Typography variant="subtitle1" component="div">
Selected: {selectedValue}
</Typography>
<br />
<Button variant="outlined" onClick={handleClickOpen}>
Open simple dialog
</Button>
<SimpleDialog
selectedValue={selectedValue}
open={open}
onClose={handleClose}
/>)```

用户提供的问题：

###
编写一个响应式布局的博客发表页，需要包含标题、内容、时间、存为草稿，如果创建成功则弹出对话框，显示：“创建成功!”
###

页面布局要求：

```design
-------------------------------------------------------------------------------------
|                                      Title (12x)                                   |
-------------------------------------------------------------------------------------
|                                      Content (12x)                                 |
-------------------------------------------------------------------------------------
| Time (6x) | Save as Draft (6x) | Submit (6x) | Clear (6x) |                          |
-------------------------------------------------------------------------------------
```

现在请你生成前端代码，代码使用 Markdown 语言编写，以便用户可以直接复制到项目中。
