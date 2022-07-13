package jt.projects.gbweatherapp.utils.draw

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.squareup.picasso.Transformation


class CircleTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val path = Path()      // Определяем шаблон обрезки...
        path.addCircle( // ...как круг
            (source.width / 2).toFloat(),
            (source.height / 2).toFloat(),
            (source.width / 2).toFloat(),
            Path.Direction.CCW
        )
        val answerBitMap =      // Создаём битмап, который и будет результирующим
            Bitmap.createBitmap(
                source.width, source.height,
                Bitmap.Config.ARGB_8888
            )

        val canvas = Canvas(answerBitMap) // Создаём холст для нового битмапа
        canvas.clipPath(path) // Обрезаем холст по кругу (по шаблону)

        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)// А теперь рисуем на этом холсте исходное изображение
        canvas.drawBitmap(source, 0f, 0f, paint)
        source.recycle()
        return answerBitMap
    }

    override fun key(): String {
        return "circle"
    }
}