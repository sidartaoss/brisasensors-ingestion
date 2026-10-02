# BrisaSensors · Ingestão

Serviço de ingestão do [BrisaSensors](https://github.com/sidartaoss/brisasensors), sistema fictício de monitoramento da qualidade do ar em ambientes internos. Recebe as leituras dos dispositivos via MQTT, HTTP e LoRaWAN, com um adaptador por protocolo como módulo interno, valida e normaliza cada leitura em um evento canônico e o publica no tópico de leituras, particionado pelo identificador do dispositivo. Não guarda estado e escala horizontalmente.

As fronteiras e decisões do serviço estão no [estudo de caso](https://github.com/sidartaoss/fronteiras-de-microsservicos#7-estudo-de-caso-brisasensors).
