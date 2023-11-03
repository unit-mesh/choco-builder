---
layout: default
title: 轻量级客户端模型
parent: Patterns
nav_order: 100
---

## 轻量级 embedding 模型

## 轻量级 Code completion 模型

JetBrains 采用的轻量级模型：

```xml
<models version="1">
  <model>
    <version>0.1.62-native-onnx</version>
    <size>161538626</size>
    <languages>
      <language>python</language>
    </languages>
    <binary>flcc.model</binary>
    <bpe>flcc.bpe</bpe>
    <config>flcc.json</config>
    <native>
      <archive>full-line-inference.zip</archive>
      <version>0.2.45</version>
    </native>
    <changelog>- 100M model for Python trained on Starcoder dataset, HF optimal training duration</changelog>
  </model>
  <model>
    <version>0.1.35-native-onnx</version>
    <size>161543029</size>
    <languages>
      <language>kotlin</language>
    </languages>
    <binary>flcc.model</binary>
    <bpe>flcc.bpe</bpe>
    <config>flcc.json</config>
    <native>
      <archive>full-line-inference.zip</archive>
      <version>0.2.45</version>
    </native>
    <changelog>- Giga Brand new Kotlin model from hack, but quantize :)</changelog>
  </model>
</models>
```
