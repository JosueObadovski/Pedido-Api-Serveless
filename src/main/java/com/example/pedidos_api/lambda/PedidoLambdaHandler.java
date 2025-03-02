package com.example.pedidos_api.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.stereotype.Component;

@Component
public class PedidoLambdaHandler implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        context.getLogger().log("Recebido input: " + input);
        return "Processado: " + input;
    }
}
