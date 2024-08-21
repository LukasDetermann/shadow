package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Documented;
import io.determann.shadow.api.shadow.modifier.C_AccessModifiable;
import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

public interface C_Field extends C_Variable,
                                 C_AccessModifiable,
                                 C_FinalModifiable,
                                 C_StaticModifiable,
                                 C_Documented
{
}
