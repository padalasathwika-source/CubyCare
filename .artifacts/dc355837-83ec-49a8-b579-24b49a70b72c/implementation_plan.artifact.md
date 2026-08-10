# Fix Android Resource Linking Error

The project is failing to build because it references Material Components themes and attributes in `themes.xml` without having the `com.google.android.material:material` and `androidx.appcompat:appcompat` dependencies included in the project.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///C:/Users/bhask/AndroidStudioProjects/CubyCare/gradle/libs.versions.toml)
- Add versions for `appcompat` and `material`.
- Add library definitions for `androidx-appcompat` and `material`.

#### [MODIFY] [build.gradle.kts](file:///C:/Users/bhask/AndroidStudioProjects/CubyCare/app/build.gradle.kts)
- Add `libs.androidx.appcompat` and `libs.material` to the `dependencies` block.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify that resource linking succeeds and the app builds.

### Manual Verification
- None required as this is a build-time fix.
