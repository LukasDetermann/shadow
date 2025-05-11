package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Annotationable;

/// A Receiver is a way to annotate `this` in an inner class [Constructor][C_Constructor] or instance [Method][C_Method]
/// @see io.determann.shadow.api.Operations#EXECUTABLE_GET_RECEIVER
/// @see io.determann.shadow.api.Operations#RECEIVER_GET_TYPE
/// @see io.determann.shadow.api.Operations#ANNOTATIONABLE_GET_ANNOTATION_USAGES
public interface C_Receiver extends C_Annotationable
{
}
