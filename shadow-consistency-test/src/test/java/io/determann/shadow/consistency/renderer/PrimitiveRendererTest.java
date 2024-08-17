package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;

class PrimitiveRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveBoolean())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(boolean.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveByte())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(byte.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveShort())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(short.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveInt())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(int.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveLong())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(long.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveChar())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(char.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveFloat())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(float.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      ConsistencyTest.<Primitive>compileTime(context -> context.getConstants().getPrimitiveDouble())
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(double.class))
                     .test(aClass -> render(DEFAULT, aClass).type());
   }
}