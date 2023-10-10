---
layout: default
title: Design DSL
parent: Domain Specific Language
nav_order: 1
permalink: /dsl/design
---

tech stacks: Antlr4, based on: [https://github.com/phodal/design](https://github.com/phodal/design)

- 语法文件：[Design.g4](https://github.com/unit-mesh/chocolate-factory/blob/master/dsl/design/src/main/antlr/Design.g4)
- 渲染组件：[design-dsl-render.tsx](https://github.com/unit-mesh/chocolate-factory/blob/master/choc-ui/components/dsl/design-dsl-render.tsx)

# Design 语法

## 组件声明

```design
page HomePage {
    LayoutGrid: 12x
    LayoutId: HomePage
    Router: "/home"
}

component Navigation {
    LayoutId: Navigation
}

component TitleComponent {}
component ImageComponent {
    Size: 1080px
}
component BlogList {
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
    BlogDetail, Space8
}
```

## Layout

```design
Layout Navigation {
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
}
```

## 用户交互流

```design
flow login {
    SEE HomePage
    DO [Click] "Login".Button
        REACT Success: SHOW "Login Success".Toast with ANIMATE(bounce)
        REACT Failure: SHOW "Login Failure".Dialog

    SEE "Login Failure".Dialog
    DO [Click] "ForgotPassword".Button
        REACT: GOTO ForgotPasswordPage

    SEE ForgotPasswordPage
    DO [Click] "RESET PASSWORD".Button
        REACT: SHOW "Please Check Email".Message
}
```

## 库/基础元素定义

```design
library FontSize {
    H1 = 18px
    H2 = 16px
    H3 = 14px
    H4 = 12px
    H5 = 10px
}

library Color {
    Primary {
        label = "Primary"
        value = "#E53935"
    }
    Secondary {
        label = "Blue"
        value = "#1E88E5"
    }
}

library Button {
    Default [
        FontSize.H2, Color.Primary
    ]
    Primary [
        FontSize.H2, Color.Primary
    ]
}
```

# 其它相关资料


refs:

- Apple's [Understanding Auto Layout](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/AutolayoutPG/index.html)
- [AutoLayout.js](https://github.com/IjzerenHein/autolayout.js)
- [Sketch-n-Sketch](https://github.com/ravichugh/sketch-n-sketch)
