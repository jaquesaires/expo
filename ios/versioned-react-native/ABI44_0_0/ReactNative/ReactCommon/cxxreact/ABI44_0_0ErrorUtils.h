/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#include <ABI44_0_0jsi/ABI44_0_0jsi.h>

namespace ABI44_0_0facebook {
namespace ABI44_0_0React {

inline static void
handleJSError(jsi::Runtime &runtime, const jsi::JSError &error, bool isFatal) {
  auto errorUtils = runtime.global().getProperty(runtime, "ErrorUtils");
  if (errorUtils.isUndefined() || !errorUtils.isObject() ||
      !errorUtils.getObject(runtime).hasProperty(runtime, "reportFatalError") ||
      !errorUtils.getObject(runtime).hasProperty(runtime, "reportError")) {
    // ErrorUtils was not set up. This probably means the bundle didn't
    // load properly.
    throw jsi::JSError(
        runtime,
        "ErrorUtils is not set up properly. Something probably went wrong trying to load the JS bundle. Trying to report error " +
            error.getMessage(),
        error.getStack());
  }

  // TODO(janzer): Rewrite this function to return the processed error
  // instead of just reporting it through the native module
  if (isFatal) {
    auto func = errorUtils.asObject(runtime).getPropertyAsFunction(
        runtime, "reportFatalError");

    func.call(runtime, error.value());
  } else {
    auto func = errorUtils.asObject(runtime).getPropertyAsFunction(
        runtime, "reportError");

    func.call(runtime, error.value());
  }
}

} // namespace ABI44_0_0React
} // namespace ABI44_0_0facebook
