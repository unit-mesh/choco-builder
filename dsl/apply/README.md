# Apply

Apply 用于 Debug prompt，可以获取远程的数据，解析，并用在 prompt 中，还可以获取数据。

## Usage

```groovy
val data = get("").header().to()
val relevant = semantic("").search().to()

// template
val step1 = """

""".send();

// println(step1)

val step2 = """

""".send();
```

HttpAction: get, post, put, delete

dataframe: to `data class`



