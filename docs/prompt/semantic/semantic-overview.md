---
layout: default
title: Semantic 搜索：总览
parent: Prompt logs
nav_order: 30
---


```debug
查询条件：

question: Semantic Workflow 是如何实现的？
englishQuery: How is Semantic Workflow implemented?
originLanguageQuery: Semantic Workflow 是如何实现的？
hypotheticalDocument:
public class SemanticWorkflow {
   private WorkflowEngine engine;
   
   public SemanticWorkflow() {
      engine = new WorkflowEngine();
   }
   
   public void executeWorkflow(Workflow workflow) {
      // Perform semantic analysis on the workflow
      SemanticAnalyzer analyzer = new SemanticAnalyzer();
      Workflow analyzedWorkflow = analyzer.analyze(workflow);
      
      // Execute the analyzed workflow using the workflow engine
      engine.execute(analyzedWorkflow);
   }
}

代码片段：

0.74799836 abstract class Workflow {    /**     * ChatWebContext is a context from GUI     */    val chatWebContext: ChatWebContext? = null    /**     * provider prompt list for debug in GUI     * 提供给 GUI 的 workflow 信息，用于调试     */    open val stages: LinkedHashMap<StageContext.Stage, StageContext> = linkedMapOf()    /**     * execute the stages of workflow     */    abstract fun execute(prompt: StageContext, chatWebContext: ChatWebContext): Flowable<WorkflowResult>    fun toFlowableResult(answerFlowable: Flowable<Answer>): Flowable<WorkflowResult> {        return Flowable.create({ emitter ->            answerFlowable.subscribe({                val result = WorkflowResult(                    currentStage = StageContext.Stage.Execute,                    nextStage = StageContext.Stage.Done,                    responseMsg = it.values.toString(),                    resultType = String::class.java.toString(),                    result = "",                    isFlowable = true,                )                emitter.onNext(result)            }, {                emitter.onError(it)            }, {                emitter.onComplete()            })        }, io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER)    }
0.7424118 class SemanticProblemAnalyzer(    private val completion: LlmProvider): ProblemAnalyzer {    override fun analyze(domain: String, question: String): ExplainQuery {        val stageContext = CodeSemanticWorkflow.ANALYSIS        val systemPrompt = stageContext.format()        val messages = listOf(            LlmMsg.ChatMessage(LlmMsg.ChatRole.System, systemPrompt),            LlmMsg.ChatMessage(LlmMsg.ChatRole.User, question),        ).filter { it.content.isNotBlank() }        FEProblemClarifier.log.info("Clarify messages: {}", messages)        val completion = completion.completion(messages)        FEProblemClarifier.log.info("Clarify completion: {}", completion)        return ExplainQuery.parse(question, completion)    }
0.7203416 @Componentclass SpecDomainDecl : DomainDeclaration {    override val domainName: String get() = "spec"    override val description: String get() = "查询系统相关的规范等。"    override fun workflow(question: String): Workflow {        return SpecWorkflow()    }
0.7125772 class CodeSemanticWorkflowTest {    @Test    fun should_output_analysis_prompt_for_testing() {        val context = CodeSemanticWorkflow.ANALYSIS        println(context.format())    }
0.7088447 interface DomainDeclaration {    /**     * A domain name should be unique and lower case, also need to be human readable.     */    val domainName: String    val description: String    fun workflow(question: String): Workflow
0.7089157 @Componentclass CodeSemanticDecl : DomainDeclaration {    override val domainName: String get() = SupportedDomains.CodeSemanticSearch.value    override val description: String get() = "语义化的代码搜索，以帮助你更好的理解代码库。"    override fun workflow(question: String): Workflow {        return CodeSemanticWorkflow()    }
0.71418244 // canonicalName: cc.unitmesh.cf.domains.semantic.flow.SemanticSolutionExecutor
0.7220461 // canonicalName: cc.unitmesh.dsl.design.DesignAppListener
0.7473141 // canonicalName: cc.unitmesh.cf.domains.interpreter.CodeInterpreterWorkflow
0.8198033 // canonicalName: cc.unitmesh.cf.domains.semantic.CodeSemanticWorkflow
```