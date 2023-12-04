package cc.unitmesh.processor.api.render

import cc.unitmesh.processor.api.ApiProcessorDetector
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class MarkdownTableRenderTest {
    @Test
    fun should_handle_to_table() {
        val file2 = File("src/test/resources/testsets/swagger-3.yaml")
        val processor2 = ApiProcessorDetector.detectApiProcessor(file2, true)!!
        val apiDetails = processor2.convertApi()

        val render = MarkdownTableRender()
        val result = render.render(apiDetails)

        val expected =
"""
## Cashback campaign

| API | Method | Description | Request | Response | Error Response |
| --- | --- | --- | --- | --- | --- |
| /cashback-campaigns/{campaignId} | GET | Retrieves a specific cashback campaign.   The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string, campaignId: string | [200: {campaignId: string, campaignName: string, startDate: string, endDate: string, status: string, remainingAmountInCents: integer}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] | 400: {"error": String} |

## Cashback

| API | Method | Description | Request | Response | Error Response |
| --- | --- | --- | --- | --- | --- |
| /cashback-campaigns/{campaignId}/cashbacks | GET | Retrieves a list of cashbacks based on the parameters that are provided.   The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string, campaignId: string, pageNumber: integer, pageSize: integer, fromDateTime: string, toDateTime: string, status: string | [200: {cashbacks: array, totalElementCount: integer}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] | 400: {"error": String} |
| /cashback-campaigns/{campaignId}/cashbacks | POST | Creates a cashback.   The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string, campaignId: string | [201: {cashbackId: string, url: string, amountInCents: integer, createdDateTime: string, expiryDateTime: string, redeemedDateTime: string, status: string, referenceId: string, locationId: string, locationAddress: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] | 400: {"error": String} |
| /cashback-campaigns/{campaignId}/cashbacks/{cashbackId} | GET | Retrieves a specific cashback.   The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string, campaignId: string, cashbackId: string | [200: {cashbackId: string, url: string, amountInCents: integer, createdDateTime: string, expiryDateTime: string, redeemedDateTime: string, status: string, referenceId: string, locationId: string, locationAddress: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] | 400: {"error": String} |

## Cashback notification

| API | Method | Description | Request | Response | Error Response |
| --- | --- | --- | --- | --- | --- |
| /cashback-subscriptions | POST | Subscribes to cashback related notifications. Only one subscription can be active at any given time. When this request is repeated, the existing subscription is overwritten.  The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string | [201: {subscriptionId: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 500: {errors: array}] | 400: {"error": String} |
| /cashback-subscriptions | DELETE | Deletes a subscription.   The application must have the cashback permission to complete this request. For more information, see [Step 3 - Create an appToken](#section/Tutorial/Step-3-Create-an-appToken) or [App token](#section/Overview/App-token). | X-App-Token: string | [204: {}, 401: {errors: array}, 403: {errors: array}, 500: {errors: array}] | 400: {"error": String} |
""".trimIndent()
        assertEquals(expected, result)
    }
}
