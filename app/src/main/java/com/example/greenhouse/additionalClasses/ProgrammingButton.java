    package com.example.greenhouse.additionalClasses;

    import android.content.Context;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Paint;
    import android.util.AttributeSet;
    import android.view.View;

    import androidx.annotation.Nullable;

    import com.example.greenhouse.R;

    public class ProgrammingButton extends View {

        private Paint paint;
        private int redValue = 0;
        private int blueValue = 0;
        private String text = "";

        public ProgrammingButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ProgrammingButton(Context context) {
            super(context);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setTextSize(50f);
            paint.setTextAlign(Paint.Align.CENTER);
        }

        // Методи для оновлення даних
        public void setRed(int value) {
            this.redValue = value;
            invalidate(); // Перемалювати View
        }

        public void setBlue(int value) {
            this.blueValue = value;
            invalidate();
        }

        public void setText(String text) {
            this.text = text;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();
            float centerX = width / 2f;
            float centerY = height / 2f;

            // Розраховуємо параметри тексту та плашки заздалегідь
            paint.setTextSize(getResources().getDimension(R.dimen.text_on_programming_btn_size));
            float textHigh = paint.descent() - paint.ascent();
            float radius = textHigh * 0.6f;
            float plateHalfHeight = textHigh / 3.5f; // Половина висоти сірої плашки

            // --- МАЛЮЄМО ПРОГРЕС (RED - зверху) ---
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.soft_red));

            // Початок червоного: верхній край сірої плашки
            float redStart = centerY - plateHalfHeight;
            // Кінець червоного: залежить від redValue (йде вгору до 0)
            float redEnd = redStart - (redValue / 100f) * redStart;
            canvas.drawRect(0, redEnd, width, redStart, paint);

            // --- ПРОГРЕС (BLUE - знизу) ---
            paint.setColor(getResources().getColor(R.color.soft_blue));

            // Початок синього: нижній край сірої плашки
            float blueStart = centerY + plateHalfHeight;
            // Відстань від плашки до низу кнопки
            float availableBlueSpace = height - blueStart;
            // Кінець синього: додаємо відсоток від доступного простору
            float blueEnd = blueStart + (blueValue / 100f) * availableBlueSpace;
            canvas.drawRect(0, blueStart, width, blueEnd, paint);

            // --- СІРА ПЛАШКА ТА КОЛО ---
            paint.setColor(getResources().getColor(R.color.programming_btn_text_bg));
            canvas.drawRect(0, centerY - plateHalfHeight, width, centerY + plateHalfHeight, paint);
            canvas.drawCircle(centerX, centerY, radius, paint);

            // --- ТЕКСТ ---
            paint.setColor(getResources().getColor(R.color.text_light));
            paint.setTextAlign(Paint.Align.CENTER);
            float textY = centerY - (paint.descent() + paint.ascent()) / 2f;
            canvas.drawText(text, centerX, textY, paint);

            // --- ЧОРНА РАМКА (1px) ---
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1f);
            // Малюємо по самому краю
            canvas.drawRect(0.5f, 0.5f, width - 0.5f, height - 0.5f, paint);
        }


    }
