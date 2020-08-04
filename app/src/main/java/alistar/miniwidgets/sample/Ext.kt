package alistar.miniwidgets.sample

import android.graphics.Point
import android.view.View

fun View.getPointOnScreen(): Point {
    val originalPos = IntArray(2)
    getLocationInWindow(originalPos)
    val x = originalPos[0] + width / 2
    val y = originalPos[1]
    val point = Point()
    point.set(x, y)
    return point
}