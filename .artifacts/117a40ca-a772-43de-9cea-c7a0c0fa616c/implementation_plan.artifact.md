# Fix Android Resource Linking Error

The project is failing to build because it references Material Components (Material 2) themes and attributes in `themes.xml`, but the `com.google.android.material:material` dependency is missing from the project.

## Proposed Changes

### [Component Name] Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/bhask/AndroidStudioProjects/CubyCare/gradle/libs.versions.toml)
- Add `material` version.
- Add `material` library definition.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/bhask/AndroidStudioProjects/CubyCare/app/build.gradle.kts)
- Add `implementation(libs.material)` to the dependencies block.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify that the resource linking task succeeds.

### Manual Verification
- Verify that the app builds and can be deployed.
