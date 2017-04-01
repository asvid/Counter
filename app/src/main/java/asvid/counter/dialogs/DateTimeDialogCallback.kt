package asvid.counter.dialogs

import java.util.Date

interface DateTimeDialogCallback {
    fun onCancel()
    fun onDateSelected(newDate: Date)
}