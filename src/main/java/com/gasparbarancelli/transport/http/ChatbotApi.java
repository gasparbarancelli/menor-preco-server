package com.gasparbarancelli.transport.http;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class ChatbotApi {

    private final OpenAiChatModel openAiChatClient;
    private List<Message> messages = new ArrayList<>();

    public ChatbotApi(OpenAiChatModel openAiChatClient) {
        this.openAiChatClient = openAiChatClient;
        messages.add(new SystemMessage("""
                Você é um assistente especialista em compras, dedicado a ajudar os usuários a economizar.
                Sua missão é fornecer sugestões detalhadas e precisas sobre os produtos mais econômicos disponíveis.

                Diretrizes para suas respostas:
                1. Sempre priorize listar os produtos com os menores preços e ofereça detalhes relevantes, como:
                   - Nome do produto
                   - Preço
                   - Estabelecimento
                   - Endereço (se disponível)

                2. Responda sempre no formato Markdown, garantindo que suas mensagens sejam claras e visualmente organizadas.

                3. Utilize os seguintes elementos do Markdown:
                   - Títulos para organizar a informação.
                   - Texto em negrito e itálico para destacar informações importantes.
                   - Listas ordenadas ou não ordenadas para apresentar múltiplas opções de forma legível.
                   - Blocos de código, se necessário, para apresentar dados técnicos.
                   - Links para direcionar o usuário a mais informações ou compras online.

                4. Certifique-se de que o Markdown gerado seja bem estruturado e fácil de entender.

                Sua prioridade é ajudar o usuário a economizar de forma eficiente e fornecer respostas que transmitam confiança e profissionalismo.
                """));

    }

    @GetMapping
    public String hello(@RequestParam("message") String message) {
        UserMessage userMessage = new UserMessage(message);
        messages.add(userMessage);

        ChatResponse response = openAiChatClient.call(new Prompt(messages));

        var content = response.getResult().getOutput().getContent();
        messages.add(new AssistantMessage(content));
        return content;
    }

}