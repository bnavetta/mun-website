import "./home.css";

const MS_PER_DAY = 1000 * 60 * 60 * 24;
const MS_PER_HOUR = 1000 * 60 * 60;
const MS_PER_MINUTE = 1000 * 60;
const MS_PER_SECOND = 1000;

const countdownDate = new Date("November 6, 2020 13:00:00").getTime();

document.addEventListener("DOMContentLoaded", () => {
    const target = document.querySelector(".home--countdown");

    const daysElt = target.querySelector(".home--countdown-days");
    const hoursElt = target.querySelector(".home--countdown-hours");
    const minutesElt = target.querySelector(".home--countdown-minutes");
    const secondsElt = target.querySelector(".home--countdown-seconds");

    setInterval(() => {
        const now = new Date().getTime();

        const delta = countdownDate - now;

        const days = Math.floor(delta / MS_PER_DAY);
        const hours = Math.floor((delta % MS_PER_DAY) / MS_PER_HOUR);
        const minutes = Math.floor((delta % MS_PER_HOUR) / MS_PER_MINUTE);
        const seconds = Math.floor((delta % MS_PER_MINUTE) / MS_PER_SECOND);

        daysElt.innerHTML = days.toString().padStart(3, "0");
        hoursElt.innerHTML = hours.toString().padStart(2, "0");
        minutesElt.innerHTML = minutes.toString().padStart(2, "0");
        secondsElt.innerHTML = seconds.toString().padStart(2, "0");
    }, 1000);
});
