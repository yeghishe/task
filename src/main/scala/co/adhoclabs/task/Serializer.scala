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
    try {
      o.writeObject(obj)
      o.flush()
      b.toByteArray
    } finally {
      o.close()
      b.close()
    }
  }

  def deserialize(buf: Array[Byte]): Any = {
    val b = new ByteArrayInputStream(buf)
    val o = new ObjectInputStream(b)
    try {
      o.readObject()
    } finally {
      o.close()
      b.close()
    }
  }
}