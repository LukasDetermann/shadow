package io.determann.shadow.api.renderer;

public interface ModuleRenderer
{
   /**
    * <pre>{@code
    * module my.module {
    *     requires transitive some.thing;
    *
    *     exports something.toExport;
    *
    *     uses important.stuff;
    * }
    * }</pre>
    */
   String declaration();
}
