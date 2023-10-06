package com.capco.hackathon2023;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

class LogAnalysis {
    private static final String AZURE_OPENAI_API_KEY = "accbec4a886f4c9c9dfafc861931841f";
    private static final String AZURE_OPENAI_LANGUAGE_ENDPOINT = "https://log-analysis-resource.openai.azure.com/";

    public static void main(String[] args) {
        System.out.println("Hello world!");

        String deploymentName = "log-analysis-deployment";

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(AZURE_OPENAI_LANGUAGE_ENDPOINT)
                .credential(new AzureKeyCredential(AZURE_OPENAI_API_KEY))
                .buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, "You are a helpful assistant"));
        chatMessages.add(new ChatMessage(ChatRole.USER, "Could you generate Java method that calculates Fibbonacci sequence?"));
//        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT, "Yes, customer managed keys are supported by Azure OpenAI?"));
//        chatMessages.add(new ChatMessage(ChatRole.USER, "Do other Azure AI services support this too?"));


        ChatCompletions chatCompletions = client.getChatCompletions(deploymentName, new ChatCompletionsOptions(chatMessages));

        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
