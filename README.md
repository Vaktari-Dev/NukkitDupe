# NukkitDupe

![Version](https://img.shields.io/badge/version-1.0.0-brightgreen)
![Nukkit](https://img.shields.io/badge/Nukkit-PM1E-blue)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![License](https://img.shields.io/badge/license-MIT-green)

**NukkitDupe** — это production-ready плагин для умного дублирования предметов на серверах **Nukkit-PM1E**. Разработан как аналог систем дюпа с Lumi/Nukkit-MOT, полностью адаптирован под современные версии Nukkit.

---

## Особенности

- **Умный дюп** — автоматически определяет максимальный стак предмета
- **Поддержка стаков** — 64 для обычных предметов, 16 для снежков/яиц/жемчуга, учёт нестандартных stack size
- **Система кулдаунов** — глобальный и групповые кулдауны с bypass permission
- **Система лимитов** — лимит использований за рестарт, групповые лимиты, bypass permission
- **Blacklist** — запрет дюпа определённых предметов по ID с bypass permission
- **Multipass Integration** — автоматическое определение и интеграция с Multipass
- **Безопасность** — защита от дюпа запрещённых предметов
- **Гибкая конфигурация** — config.yml + messages.yml с цветовыми кодами
- **Clean Architecture** — managers/services/listeners, никакого deprecated API

---

## Установка

1. Скачайте последний релиз `NukkitDupe.jar` со страницы [releases](https://github.com/Vaktari-Dev/NukkitDupe/releases)
2. Поместите `.jar` файл в папку `plugins/` вашего сервера
3. Перезапустите сервер или выполните `/reload`
4. Настройте `plugins/NukkitDupe/config.yml` под себя
5. Готово! 🎉

### Требования

- **Java** 17 или выше
- **Nukkit-PM1E** latest
- **Multipass** (опционально, для расширенной интеграции)

---

## Команды

| Команда | Описание | Permission |
|---------|----------|------------|
| `/dupe` | Дублировать предмет в руке | `nukkitdupe.use` |
| `/dupe reload` | Перезагрузить конфигурацию | `nukkitdupe.admin` |
| `/dupe reset` | Сбросить лимиты и кулдауны | `nukkitdupe.admin` |

---

## Permissions

| Permission | Описание | Default |
|------------|----------|---------|
| `nukkitdupe.use` | Доступ к команде `/dupe` | `op` |
| `nukkitdupe.admin` | Доступ к `/dupe reload`, `/dupe reset` | `op` |
| `nukkitdupe.bypass.cooldown` | Игнорирование кулдауна | `false` |
| `nukkitdupe.bypass.limit` | Игнорирование лимита использований | `false` |
| `nukkitdupe.bypass.blacklist` | Игнорирование blacklist | `false` |

### Групповые permission

Группы настраиваются в `config.yml` и активируются permission:

`nukkitdupe.group.<name>`

Например, для группы vip: `nukkitdupe.group.vip`

---

## Конфигурация

### config.yml

```yaml
settings:
  cooldown: 30
  default-limit: 10

groups:
  vip:
    cooldown: 15
    limit: 25
  premium:
    cooldown: 5
    limit: 50
  admin:
    cooldown: 0
    limit: -1

blacklist:
  - "minecraft:barrier"
  - "minecraft:command_block"
  - "minecraft:bedrock"
```

### messages.yml

```yaml
prefix: "&8[&bNukkitDupe&8] &r"

dupe-success: "{prefix}&aВы успешно задюпали &e{item}&a x&e{amount}&a!"
dupe-cooldown: "{prefix}&cПодождите &e{time}&c секунд перед следующим дюпом."
dupe-limit-reached: "{prefix}&cВы исчерпали лимит дюпов (&e{limit}&c)."
dupe-blacklisted: "{prefix}&cЭтот предмет нельзя дюпать."
dupe-no-item: "{prefix}&cВозьмите предмет в руку."
```

Поддерживаются цветовые коды `&0-&f`, `&l`, `&m`, `&n`, `&o`, `&r`.

---

## Журнал изменений

### 1.0.0
- Первый стабильный релиз
- Умный дюп с автоопределением стака
- Система кулдаунов и лимитов
- Blacklist предметов
- Multipass интеграция
- Полная конфигурация через YAML

---

## Сборка из исходников

```bash
git clone https://github.com/Vaktari-Dev/NukkitDupe.git
cd NukkitDupe
mvn clean package
```

Скомпилированный `.jar` появится в папке `target/`.

---

## Разработчик

**FarukDevv**

- GitHub: [@FarukDevv](https://github.com/FarukDevv)

---

## Лицензия

Этот проект распространяется под лицензией MIT. Подробнее — в файле `LICENSE`.
