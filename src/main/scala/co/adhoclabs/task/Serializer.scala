package co.adhoclabs.task

import java.io.{ ByteArrayInputStream, ObjectInputStream, ByteArrayOutputStream, ObjectOutputStream }

/**
 * Created by yeghishe on 5/4/15.
 */
@SuppressWarnings(Array("MethodReturningAny"))
private[task] object Serializer {
  def serialize(obj: Any): Array[Byte] = {
    val b = new ByteArrayOutputStream()
    val o = new ObjectOutputStream(b)
    o.writeObject(obj)
    val result = b.toByteArray

    o.close()
    b.close()
    result
  }

  def deserialize(buf: Array[Byte]): Any = {
    val b = new ByteArrayInputStream(buf)
    val o = new ObjectInputStream(b)
    val result = o.readObject()

    o.close()
    b.close()
    result
  }
}
