---
layout: default
title: Design DSL
parent: DSL
nav_order: 1
permalink: /dsl/design
---

# Design DSL

based on: [https://github.com/phodal/design](https://github.com/phodal/design)

refs:

- Apple [Understanding Auto Layout](https://developer.apple.com/library/archive/documentation/UserExperience/Conceptual/AutolayoutPG/index.html)
- [AutoLayout.js](https://github.com/IjzerenHein/autolayout.js)


## Examples

```design
Layout Navigation {
--------------------------------------
| "home" |"detail" | Button("Login") |
--------------------------------------
}

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