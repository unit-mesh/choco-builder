---
layout: default
title: Design DSL
parent: Domain Specific Language
nav_order: 1
permalink: /dsl/design
---

based on: [https://github.com/phodal/design](https://github.com/phodal/design)

tech stacks: Antlr4

refs:

- Apple's [Understanding Auto Layout](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/AutolayoutPG/index.html)
- [AutoLayout.js](https://github.com/IjzerenHein/autolayout.js)
- [Sketch-n-Sketch](https://github.com/ravichugh/sketch-n-sketch)

## DSLs

Elements

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

Layout Examples:

```design
Layout Navigation {
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
}
```

Task Flows / User Flows Example

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

Library Defines Examples

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