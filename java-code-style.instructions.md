# Java Code Style

This document defines the Java code style for this project. Apply the shared rules first, then the rules for the relevant class type. Preserve the purpose and behavior of each class.

## Shared Rules

### Visibility and qualification

- Keep fields `private` by default.
- Use `protected` only when a subclass is intentionally expected to access a member.
- Use `public` for the class's intended external API. Package-private visibility is appropriate for members used only within the package.
- Use `this.` when it resolves ambiguity, such as when a constructor parameter shadows a field. Omit it when it adds no clarity.

### Formatting

- Keep each line within 120 characters.
- Always use braces for control-flow blocks.
- Write every method as a multi-line block, with at least three lines including its signature and closing brace.
- Leave one blank line between methods in the same logical group.
- Leave two blank lines between logical groups.
- Leave a blank line after the class's final method.
- Put each annotation on its own line immediately above the annotated declaration. Do not put blank lines between consecutive annotations.
- Break long method-call chains vertically where that makes the steps easier to read. Do not wrap a chain mechanically while keeping an unnecessarily long chain on one line.

### Section headings and comments

Some class types use named sections; other classes use visual spacing only. Follow the rule for the class type below.

- A top-level section heading has no indentation and uses the exact form `//----SectionName----`, with no spaces between `//` and the text.
- Leave two blank lines before every top-level section heading.
- A subsection heading is indented to the same column as the methods it labels and uses the form `// Sub-section name`, with one space after `//`.
- Do not add the two-blank-line top-level heading spacing before subsections.
- General comment and Javadoc content has no project-wide standard yet. Do not add obvious comments or invent a new comment convention while performing style cleanup.

## Page Classes

### Fields and constructors

- Arrange fields in this order when present: logger, URL, fields for built-in components, then page locators.
- Group page locators by the logical regions of the site's page.
- If a region has a root locator stored as a field, put it first in that region's locator group.
- Locator prefixes describe the element's functional role, not its HTML tag. For example, a link that functions as a button uses the `btn` prefix.
- Keep locator prefixes at four characters or fewer. For longer words, use up to four anchor consonants, as in `slct`.
- Reuse an established prefix consistently for equivalent elements. Agree on a prefix when a new element role is ambiguous; do not invent an inconsistent variant.
- Place all constructors after the fields and before other methods. Keep overloaded constructors together, with each subsequent constructor directly below the preceding one.

### Methods and sections

- Use `//----Internal implementation----` for private helper methods when that section is non-empty.
- Use `//----Public Interface----` for the public API section.
- In the public interface, place `getTargetUrl()` before `getRequiredElements()`.
- `getRequiredElements()` represents only elements that are both required and invariant on the page. Hidden or optional elements do not belong in the list. Determining which elements qualify requires inspecting the actual site; do not infer or change this list from source code alone.
- Group the remaining page methods according to the content and behavior of the actual page. Do not force every page into the same subsection layout.
- When a page's actions form one small, simple group, use the subsection `// Page actions`.
- If composite actions exist, put them in `// Composite actions` near the end of the public interface.
- If methods expose built-in page components, put them in `// Built-in components`, which is the final subsection of the public interface. When both special subsections exist, `// Composite actions` comes immediately before `// Built-in components`.
- Add these special subsections only when the corresponding methods exist.

## Component Classes

### Fields and constructors

- Arrange fields around the logical parts of the component. If a component region has a root locator stored as a field, put it first in that region's locator group.
- Apply the shared locator-prefix rules used by page classes.
- Place all constructors after the fields and before other methods. Keep overloaded constructors together, with each subsequent constructor directly below the preceding one.
- An item component is created by its parent component when needed. Do not integrate item components as permanent fields of the parent component.

### Methods and sections

- Use `//----Internal implementation----` for private helper methods when that section is non-empty.
- Use `//----Public Interface----` for the public API section.
- `getRequiredElements()` represents only elements that are both required and invariant for the component. Determining which elements qualify requires inspecting the actual site; do not infer or change this list from source code alone.
- Group the remaining methods according to the component's purpose. Do not impose one getter/action order or subsection structure on every component.
- When a component's actions form one small, simple group, use the subsection `// Actions`.

## Test Classes

These rules apply to UI tests, API tests, and base test classes.

- Keep test-class fields `private`.
- Order members as follows: fields; lifecycle methods such as `@Before...` and `@After...`; test methods; private helper methods.
- If private helper methods exist, put them in the final top-level section named `//----Private helpers----`.
- Apply the same member order to base test classes.
- Do not include `AutomationExerciseOriginalTestCases` in this cleanup standard.

## Account Flows

These rules apply to every class in the `flows` package.

- Use top-level section headings for every non-empty section. Do not divide sections into subsections.
- Put a comment stating the required precondition immediately before every method.
- Preconditions depend on the actual site's visible state. Do not invent, infer, or rewrite a precondition based only on source code. Preserve existing preconditions; report uncertain or missing ones for human review.

## Page Assertions

- Use top-level section headings for every non-empty section. Do not divide sections into subsections.
- Expose assertion operations through static methods of the `PageAssertions` class.

## Other Java Classes

- Apply the shared rules and organize fields, constructors, and methods in a way that fits the class's responsibility.
- Use blank lines and indentation to separate logical groups. Do not add top-level or subsection heading comments to these classes.
- Do not force a single member order on unrelated class types.
