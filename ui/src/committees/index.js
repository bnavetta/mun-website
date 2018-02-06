import "./committees.css";

document.addEventListener("DOMContentLoaded", () => {
    const sections = Array.from(document.querySelectorAll(".chunk"));
    const labels = Array.from(
        document.querySelectorAll(".committee-menu > li")
    );
    console.log("Sections", sections);
    console.log("Labels", labels);

    function refresh() {
        const scrollPos = document.documentElement.scrollTop;

        let lastShown = -1;
        for (let i = 0; i < sections.length; i++) {
            const section = sections[i];

            // Use some lead amount for what's roughly on-screen
            if (scrollPos >= section.offsetTop - 50) {
                lastShown = i;
            }

            // if (scrollPos >= section.offsetTop) {
            //     labels[i].classList.add('active')
            // } else {
            //     labels[i].classList.remove('active')
            // }
        }

        for (let i = 0; i < labels.length; i++) {
            if (i === lastShown) {
                labels[i].classList.add("active");
            } else {
                labels[i].classList.remove("active");
            }
        }
    }

    document.addEventListener("scroll", refresh);
});
