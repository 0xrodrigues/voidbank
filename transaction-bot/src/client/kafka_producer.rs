use rdkafka::config::ClientConfig;
use rdkafka::error::KafkaError;
use rdkafka::producer::{FutureProducer, FutureRecord};

pub async fn send_to_kafka(topic: &str, payload: &str) -> Result<(), KafkaError> {
    let producer: FutureProducer = ClientConfig::new()
        .set("bootstrap.servers", "localhost:9092")
        .create()
        .expect("Producer creation error");

    let record = FutureRecord::to(topic).payload(payload).key(&());

    // Envia a mensagem e aguarda o resultado
    let delivery_status = producer
        .send(record, std::time::Duration::from_secs(0))
        .await;

    match delivery_status {
        Ok(_) => Ok(()),
        Err((e, _)) => Err(e),
    }
}
