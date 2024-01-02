package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.consistency.ConsistencyTest.compileTime;

class PrimitiveRendererTest
{
   @Test
   void type()
   {
      compileTime(context -> context.getConstants().getPrimitiveBoolean())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(boolean.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveByte())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(byte.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveShort())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(short.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveInt())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(int.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveLong())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(long.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveChar())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(char.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveFloat())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(float.class))
                     .test(aClass -> render(DEFAULT, aClass).type());

      compileTime(context -> context.getConstants().getPrimitiveDouble())
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(double.class))
                     .test(aClass -> render(DEFAULT, aClass).type());
   }
}