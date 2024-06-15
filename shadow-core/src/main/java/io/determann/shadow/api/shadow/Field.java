package io.determann.shadow.api.shadow;

import io.determann.shadow.api.Documented;
import io.determann.shadow.api.modifier.AccessModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.StaticModifiable;

public interface Field extends Variable,
                               AccessModifiable,
                               FinalModifiable,
                               StaticModifiable,
                               Documented
{
}
