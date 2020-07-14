### Spring Webflux

- Este projeto tem por objetivo aprofundar nos operadores reactor e seu uso com spring.

#### Conceitos
- **Publisher**: quem publica os eventos (host -> não precisa de um assinante para emitir eventos ou cold -> precisa de um assinante para emitir eventos)
- **Subscribe**: assinante do fluxo de eventos.
- **Subscription**: assinatura dos eventos, por ela o assinante solicita o próximo evento (ja realiza automaticamente) ou cancela.
- **Backpressure**: contra pressão, ou seja, quando o publicador gera muitos eventos alem da capacidade efetiva do assinante consumir, evita que ocorra alguma falha por ausência de recursos.
- **Mono**: emite 0 ou 1 evento.
- **Flux**: emite 0 ou N evento.
