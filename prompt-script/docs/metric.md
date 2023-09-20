# Metric

[A Metrics-First Approach to LLM Evaluation](https://www.rungalileo.io/blog/metrics-first-approach-to-llm-evaluation)

![](https://cdn.sanity.io/images/tf66morw/production/e8a016367389494c9f56d593b7f65ccb88062fba-1440x1043.png?w=1440&h=1043&auto=format)

```yaml
name: chat_with_pdf_default_20230820_162219_559000
flow: .
data: ./data/bert-paper-qna.jsonl
#run: <Uncomment to select a run input>
column_mapping:
  chat_history: ${data.chat_history}
  pdf_url: ${data.pdf_url}
  question: ${data.question}
  config: 
    EMBEDDING_MODEL_DEPLOYMENT_NAME: text-embedding-ada-002
    CHAT_MODEL_DEPLOYMENT_NAME: gpt-35-turbo
    PROMPT_TOKEN_LIMIT: 3000
    MAX_COMPLETION_TOKENS: 1024
    VERBOSE: true
    CHUNK_SIZE: 256
    CHUNK_OVERLAP: 64
```