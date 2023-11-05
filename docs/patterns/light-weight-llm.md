---
layout: default
title: 轻量级客户端小模型
parent: Patterns
nav_order: 100
---

## 轻量级 Embedding 模型

分为了四种类型：

- actions, IDE 的 Action
- files, 项目代码文件的路径名
- classes, 项目代码的 Class 的类名
- symbols, 项目代码的其它 Symbol

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

三种模式：LLAMA_NATIVE、ONNX_NATIVE、K_INFERENCE

CLI Examples:

```bash
usage: full-line-inference [options]
options:
	[-p, --port] [args] - the port the server is listening on, if not specified the server will choose the port
	[-m, --model] [args] - [required] path to the model file
	[-t, --type] [args] - [required] model type, available values: [onnx, llama]
	[-bs, --beam-size] [args] - [required] beam size for beam search
	[-ic, --iterations-count] [args] - [required] number of iterations for beam search
	[-cr, --context-ratio] [args] - [required] an initializing context will be trimmed to (cr * max_contex) symbols
	[-mc, --min-cached] [args] - [required] minimum number of reused symbols from the cache
	[-mm, --max-missing] [args] - [required] maximum number of trimmed symbols when using the cache
	[-pp, --parent] [args] - parent process PID
	[-l, --logging-off] - this flag disables logging
	[-h, --help] - show this help message and exit
```

usage:

```bash
./full-line-inference -p 8080 -m flcc.model -t onnx -bs 5  -ic 2 -cr 0.5 -mc 5 -mm 30
onnx wrapper uses 2 thread(s)
vocab_size = 16384
n_ctx = 384
n_embd = 1024
n_head = 16
head_size = 64
n_layer = 6
Input Node Name/Shape (9):
	input_ids      , type - 7         , dim - 2 : [-1, -1]
	position_ids   , type - 7         , dim - 2 : [-1, -1]
	attention_mask , type - 7         , dim - 2 : [-1, -1]
	past_0         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	past_1         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	past_2         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	past_3         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	past_4         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	past_5         , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
Output Node Name/Shape (7):
	logits         , type - 1         , dim - 3 : [-1, -1, 16384]
	present_0      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	present_1      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	present_2      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	present_3      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	present_4      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
	present_5      , type - 1         , dim - 5 : [2, -1, 16, -1, 64]
2023-11-03 14:54:11.93039 [watchdog.cpp:27:watch] parent process PID to control: 64475
2023-11-03 14:54:11.95493 [grpc_server.cpp:26:run] server is listening on localhost:8080
[CONTROL MESSAGE] PORT 8080
```
