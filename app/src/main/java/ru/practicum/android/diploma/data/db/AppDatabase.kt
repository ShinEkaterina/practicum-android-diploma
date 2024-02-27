package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.db.covertors.ListOfStringsConverter
import ru.practicum.android.diploma.data.db.dao.VacancyDao
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(
    entities = [VacancyEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListOfStringsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vacancyDao(): VacancyDao

/*    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Создаем новую таблицу с обновленной структурой
                database.execSQL(
                    """
            CREATE TABLE favourites_table_new (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT,
                areaName TEXT,
                areaUrl TEXT,
                contactsEmail TEXT,
                contactsName TEXT,
                contactsPhones TEXT,
                description TEXT,
                employerName TEXT,
                employmentName TEXT,
                experienceName TEXT,
                keySkillsNames TEXT,
                salary TEXT,
                scheduleName TEXT
            )
        """.trimIndent()
                )

                // 3. Удаляем старую таблицу
                database.execSQL("DROP TABLE favourites_table")

                // 4. Переименовываем новую таблицу, чтобы она заменила старую
                database.execSQL("ALTER TABLE favourites_table_new RENAME TO favourites_table")
            }
        }

    }*/
}
