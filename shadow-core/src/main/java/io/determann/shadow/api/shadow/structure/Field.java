package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.modifier.AccessModifiable;
import io.determann.shadow.api.shadow.modifier.FinalModifiable;
import io.determann.shadow.api.shadow.modifier.StaticModifiable;

public interface Field extends Variable,
                               AccessModifiable,
                               FinalModifiable,
                               StaticModifiable,
                               Documented
{
}
