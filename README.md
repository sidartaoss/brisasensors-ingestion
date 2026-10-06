# BrisaSensors · Ingestão

Serviço de ingestão do [BrisaSensors](https://github.com/sidartaoss/brisasensors), sistema fictício de monitoramento da qualidade do ar em ambientes internos. Recebe as leituras dos dispositivos via MQTT, HTTP e LoRaWAN, com um adaptador por protocolo como módulo interno, valida e normaliza cada leitura em um evento canônico e o publica no tópico de leituras, particionado pelo identificador do dispositivo. Não guarda estado e escala horizontalmente.

As fronteiras e decisões do serviço estão no [estudo de caso](https://github.com/sidartaoss/fronteiras-de-microsservicos#7-estudo-de-caso-brisasensors).

## Estado da implementação

- Por ora, apenas o adaptador HTTP: `POST /api/devices/{deviceId}/readings/data`, com a concentração de CO₂ em ppm no corpo, em texto puro. Porta 8081.
- Cada leitura válida recebe um UUIDv7 e é publicada na exchange `ingestion.reading-registered.v1.e` do RabbitMQ (vhost `brisasensors`), com o identificador do dispositivo como routing key. A resposta de sucesso só sai depois da confirmação do broker; sem ela, a API responde 503, e o dispositivo reenvia.
- Como o adaptador HTTP não recebe a hora da medição, `measuredAt` assume a hora de chegada.

As decisões de código estão no guia [Implementação de Microsserviços](https://github.com/sidartaoss/implementacao-de-microsservicos#7-implementação-de-referência-brisasensors).
