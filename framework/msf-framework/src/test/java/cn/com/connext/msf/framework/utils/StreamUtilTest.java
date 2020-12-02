package cn.com.connext.msf.framework.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Stream;

public class StreamUtilTest {

    @Test
    public void distinctByKey() {
        Person p1 = new Person("BoB", "15805159459");
        Person p2 = new Person("Lucy", "15805159459");
        Person p3 = new Person("Yu", "13476867212");

        Assert.assertEquals(2, Stream.of(p1, p2, p3).filter(StreamUtil.distinctByKey(Person::getPhone)).count());
    }

    @Test
    public void testConsumer() {
        StreamUtil.consumer("输出正常", x -> System.out.println(x));
        StreamUtil.consumer( new Person("Yu", "13476867212"), x -> System.out.println(((Person)x).getName()));
        StreamUtil.consumer( 999, x -> System.out.println(Integer.parseInt(String.valueOf(x))+1));
    }

    @Test
    public void testConsumer02() {
        StreamUtil.consumer(6,"输出正常", x -> System.out.println(x));
        StreamUtil.consumer(10, new Person("Yu", "13476867212"), x -> System.out.println(((Person)x).getName()));
        StreamUtil.consumer( 10,999, x -> System.out.println(Integer.parseInt(String.valueOf(x))+1));
    }

    class Person {
        String id;
        String name;
        String phone;

        public Person(String name, String phone) {
            this.id = Base58UUID.newBase58UUID();
            this.name = name;
            this.phone = phone;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }
}