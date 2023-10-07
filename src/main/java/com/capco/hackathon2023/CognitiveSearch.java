// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.capco.hackathon2023;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.AzureChatExtensionConfiguration;
import com.azure.ai.openai.models.AzureChatExtensionType;
import com.azure.ai.openai.models.AzureCognitiveSearchChatExtensionConfiguration;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Sample demonstrates the "Azure OpenAI on your data" feature. Prerequisites and guides
 * for this feature can be found at:
 * <a href="https://learn.microsoft.com/azure/ai-services/openai/use-your-data-quickstart?tabs=command-line&pivots=programming-language-studio">Bring Your Own Data</a>
 */
class CognitiveSearch {
    /**
     * Runs the sample and demonstrates configuration of Azure Cognitive Search as a data source.
     *
     * @param args Unused. Arguments to the program.
     */

    private static final String AZURE_OPENAI_API_KEY = "accbec4a886f4c9c9dfafc861931841f";
    private static final String AZURE_OPENAI_LANGUAGE_ENDPOINT = "https://log-analysis-resource.openai.azure.com/";
    private static final String DEPLOYMENT_ID = "log-analysis-deployment";
    private static final String COGNITIVE_SEARCH_ENDPOINT = "https://hackhathon-cognitive-search.search.windows.net/";
    private static final String COGNITIVE_SEARCH_ADMIN_KEY = "4iIomx7gjgBbd7bXKArgjSls5fGoK5td7xiY9e5BTdAzSeAC9zl5";
    private static final String COGNITIVE_SEARCH_INDEX_NAME = "index-loganalytics";
    private static final Logger LOGGER = Logger.getAnonymousLogger();

    public static void main(String[] args) {

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(AZURE_OPENAI_LANGUAGE_ENDPOINT)
                .credential(new AzureKeyCredential(AZURE_OPENAI_API_KEY))
                .buildClient();

        AzureCognitiveSearchChatExtensionConfiguration cognitiveSearchConfiguration =
                new AzureCognitiveSearchChatExtensionConfiguration(
                        COGNITIVE_SEARCH_ENDPOINT,
                        COGNITIVE_SEARCH_ADMIN_KEY,
                        COGNITIVE_SEARCH_INDEX_NAME
                );

        AzureChatExtensionConfiguration extensionConfiguration =
                new AzureChatExtensionConfiguration(
                        AzureChatExtensionType.AZURE_COGNITIVE_SEARCH,
                        BinaryData.fromObject(cognitiveSearchConfiguration));


        List<ChatMessage> chatMessages = new ArrayList<>();

        Scanner userInput = new Scanner(System.in);
        String question = "";
        String endConversation = "-exit";

        LOGGER.info("Welcome to Log Analysing with ChatGPT. Please enter your requests or type '-exit' to end.\n");

        while (true) {
            LOGGER.info("Please provide input for ChatGPT...\n");
            question = userInput.nextLine();
            if (question.equals(endConversation)) {
                LOGGER.info("Ending communication with ChatGPT.");
                break;
            }

            chatMessages.add(new ChatMessage(ChatRole.USER, question));
            ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
            chatCompletionsOptions.setDataSources(Arrays.asList(extensionConfiguration));
            ChatCompletions chatCompletions = client.getChatCompletions(DEPLOYMENT_ID, chatCompletionsOptions);

            for (ChatChoice choice : chatCompletions.getChoices()) {
                ChatMessage message = choice.getMessage();
                System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
                System.out.println("Message:");
                System.out.println(message.getContent());
            }

            chatMessages.clear();

        }
    }
}