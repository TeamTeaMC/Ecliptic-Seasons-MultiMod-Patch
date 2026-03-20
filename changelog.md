# [0.27.0] Update Log / 更新日志

## 🇺🇸 English
### 🚀 Features
- **Individual Module Patch Control**
    - Added the ability to fully disable individual module patches via configuration files.
    - **Technical:** Implemented `PreloadedConfig` to read COMMON/CLIENT configs early, allowing the Mixin plugin to skip loading for disabled modules via `shouldApply(modid)`.
- **Client-Side Optimization**
    - Introduced a `clientOnly` flag for patches; several modules have been updated to skip loading on servers.
- **Improved Config Generation**
    - `PatchCore` now automatically tracks modules and populates lists for `PatchCommonConfig` and `PatchClientConfig` to generate "Enable" toggles.
- **⚠️ Note:** A **game restart is required** for these configuration changes to take effect.

---

## 🇨🇳 中文内容
### 🚀 功能更新
- **模块化补丁精细控制**
    - 允许通过配置文件完全禁用单个模块补丁。
    - **技术细节：** 引入了 `PreloadedConfig` 预加载机制，Mixin 插件可提前通过 `shouldApply(modid)` 彻底跳过已禁用模块的加载。
- **客户端专用优化**
    - 为补丁引入了 `clientOnly` 属性；部分模块已标记为仅限客户端，从而优化服务端性能。
- **自动配置生成**
    - `PatchCore` 现在会自动追踪模块列表，并由 `PatchCommonConfig` 和 `PatchClientConfig` 自动生成对应的 "Enable" 开关。
- **⚠️ 注意：** 修改配置后**需要重新启动游戏**才能生效。

---

## 📝 Configuration Example / 配置文件示例

`[stardew_fishing]`
```toml
# Module Configuration / 模块配置
[stardew_fishing]
    # Set to false to completely skip Mixin loading for this module
    # 设置为 false 将完全跳过该模块的 Mixin 加载
    Enable = false