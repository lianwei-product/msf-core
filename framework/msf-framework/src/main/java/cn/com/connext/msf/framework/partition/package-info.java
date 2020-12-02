/**********************************************
 * MQ动态分区
 * 配置开关<i>mq.partition.dynamic.enable</i>
 * 配置concurrency参数以支持一个消费端服务配置多个分区，
 * 当配置参数instanceIndex时，表示当前消费者不采用动态分区模式
 * 例如：
 * serialProducer:
 *     destination: TEST.SERIAL
 *     contentType: application/json
 *     producer:
 *         partitionKeyExpression: payload.code
 * serialConsumer:
 *     destination: TEST.SERIAL
 *     group: test
 *     contentType: application/json
 *     consumer:
 *         partitioned: true
 * #       instanceIndex: 0
 *         concurrency: 3
 *********************************************/
package cn.com.connext.msf.framework.partition;