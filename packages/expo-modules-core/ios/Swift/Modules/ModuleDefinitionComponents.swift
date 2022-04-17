import UIKit

/**
 Extends all modules with the functions used to build a module definition.
 Unfortunately they need to be scoped here, but hopefully this proposal
 https://github.com/apple/swift-evolution/blob/main/proposals/0289-result-builders.md#builder-scoped-name-lookup
 will be implemented in the future.
 */
extension AnyModule {
  // MARK: - Module name

  /**
   Sets the name of the module that is exported to the JavaScript world.
   */
  @available(*, deprecated, renamed: "Name")
  public func name(_ name: String) -> AnyDefinition {
    return ModuleNameDefinition(name: name)
  }

  // MARK: - Module's lifecycle

  /**
   Creates module's lifecycle listener that is called right after module initialization.
   */
  @available(*, deprecated, renamed: "OnCreate")
  public func onCreate(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.moduleCreate, closure)
  }

  /**
   Creates module's lifecycle listener that is called when the module is about to be deallocated.
   */
  @available(*, deprecated, renamed: "OnDestroy")
  public func onDestroy(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.moduleDestroy, closure)
  }

  /**
   Creates module's lifecycle listener that is called when the app context owning the module is about to be deallocated.
   */
  @available(*, deprecated, renamed: "OnAppContextDestroys")
  public func onAppContextDestroys(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.appContextDestroys, closure)
  }

  /**
   Creates a listener that is called when the app is about to enter the foreground mode.
   */
  @available(*, deprecated, renamed: "OnAppEntersBackground")
  public func onAppEntersForeground(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.appEntersForeground, closure)
  }

  /**
   Creates a listener that is called when the app becomes active again.
   */
  @available(*, deprecated, renamed: "OnAppBecomesActive")
  public func onAppBecomesActive(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.appBecomesActive, closure)
  }

  /**
   Creates a listener that is called when the app enters the background mode.
   */
  @available(*, deprecated, renamed: "OnAppEntersBackground")
  public func onAppEntersBackground(_ closure: @escaping () -> Void) -> AnyDefinition {
    return EventListener(.appEntersBackground, closure)
  }

  // MARK: - View Manager

  /**
   Creates the view manager definition that scopes other view-related definitions.
   */
  @available(*, deprecated, renamed: "ViewManager")
  public func viewManager(@ViewManagerDefinitionBuilder _ closure: @escaping () -> ViewManagerDefinition) -> AnyDefinition {
    return closure()
  }
}

// MARK: - Module name

/**
 Sets the name of the module that is exported to the JavaScript world.
 */
public func Name(_ name: String) -> AnyDefinition {
  return ModuleNameDefinition(name: name)
}

// MARK: - Module's lifecycle

/**
 Creates module's lifecycle listener that is called right after module initialization.
 */
public func OnCreate(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.moduleCreate, closure)
}

/**
 Creates module's lifecycle listener that is called when the module is about to be deallocated.
 */
public func OnDestroy(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.moduleDestroy, closure)
}

/**
 Creates module's lifecycle listener that is called when the app context owning the module is about to be deallocated.
 */
public func OnAppContextDestroys(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.appContextDestroys, closure)
}

/**
 Creates a listener that is called when the app is about to enter the foreground mode.
 */
public func OnAppEntersForeground(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.appEntersForeground, closure)
}

/**
 Creates a listener that is called when the app becomes active again.
 */
public func OnAppBecomesActive(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.appBecomesActive, closure)
}

/**
 Creates a listener that is called when the app enters the background mode.
 */
public func OnAppEntersBackground(_ closure: @escaping () -> Void) -> AnyDefinition {
  return EventListener(.appEntersBackground, closure)
}

// MARK: - View Manager

/**
 Creates the view manager definition that scopes other view-related definitions.
 */
public func ViewManager(@ViewManagerDefinitionBuilder _ closure: @escaping () -> ViewManagerDefinition) -> AnyDefinition {
  return closure()
}

// MARK: - Deprecated (remove deprecated `method` component once SDK44 is out)

public extension AnyModule {
  /**
   Function without arguments.
   */
  @available(*, deprecated, renamed: "function")
  func method<R>(
    _ name: String,
    _ closure: @escaping () -> R
  ) -> AnyFunction {
    return ConcreteFunction(
      name,
      argTypes: [],
      closure
    )
  }

  /**
   Function with one argument.
   */
  @available(*, deprecated, renamed: "function")
  func method<R, A0: AnyArgument>(
    _ name: String,
    _ closure: @escaping (A0) -> R
  ) -> AnyFunction {
    return ConcreteFunction(
      name,
      argTypes: [ArgumentType(A0.self)],
      closure
    )
  }

  /**
   Function with two arguments.
   */
  @available(*, deprecated, renamed: "function")
  func method<R, A0: AnyArgument, A1: AnyArgument>(
    _ name: String,
    _ closure: @escaping (A0, A1) -> R
  ) -> AnyFunction {
    return ConcreteFunction(
      name,
      argTypes: [ArgumentType(A0.self), ArgumentType(A1.self)],
      closure
    )
  }
}
