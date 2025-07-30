package io.determann.shadow.api.query.operation;

public sealed interface StaticOperation<RESULT> extends Operation<RESULT> permits StaticOperation0,
                                                                                  StaticOperation1,
                                                                                  StaticOperation2 {}