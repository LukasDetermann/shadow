package io.determann.shadow.api.shadow;

import io.determann.shadow.api.*;

public interface Package extends Nameable,
                                 QualifiedNameable,
                                 Annotationable,
                                 DeclaredHolder,
                                 ModuleEnclosed,
                                 Documented
{
}
