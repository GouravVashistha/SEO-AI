// document.addEventListener("DOMContentLoaded", function () {
//   const form = document.getElementById("search-form");
//   const input = document.getElementById("search-input");
//   const scoreBox = document.getElementById("seo-score");
//   const container = document.getElementById("details-container");
//   const loaderContainer = document.getElementById("loader-container");

//   form.addEventListener("submit", async function (e) {
//     e.preventDefault();
//     const url = input.value.trim();
//     if (!url) return;

//     try {
//       loaderContainer.style.display = "block";
//       scoreBox.textContent = "";
//       scoreBox.classList.add("loading");
//       container.innerHTML = "";

//       const response = await fetch(
//         `http://localhost:9090/api/seo/analyze?url=${encodeURIComponent(url)}`
//       );

//       loaderContainer.style.display = "none";

//       if (!response.ok) {
//         throw new Error(`HTTP error! Status: ${response.status}`);
//       }

//       const data = await response.json();
//       scoreBox.textContent = data["SEO Score"] + "/100";
//       scoreBox.classList.remove("loading");
//       renderDetails(data["seo_analysis"]);
//     } catch (err) {
//       loaderContainer.style.display = "none";
//       console.error("Fetch error:", err);
//       scoreBox.textContent = "Error!";
//       scoreBox.classList.remove("loading");
//       alert("Failed to fetch SEO data. See console for details.");
//     }
//   });

//   function renderDetails(analysis) {
//     container.innerHTML = ""; // Clear previous results

//     Object.entries(analysis).forEach(([key, value]) => {
//       const section = document.createElement("div");
//       section.className = "section-content";
//       section.id = key;

//       const title = document.createElement("div");
//       title.className = "section-title";
//       title.textContent = key.replace(/_/g, " ");

//       const content = document.createElement("div");
//       content.className = "section-data";

//       if (typeof value === "object" && !Array.isArray(value)) {
//         const table = document.createElement("table");
//         for (const [k, v] of Object.entries(value)) {
//           const row = document.createElement("tr");

//           const keyCell = document.createElement("td");
//           keyCell.textContent = formatKey(k);
//           keyCell.style.fontWeight = "bold";

//           const valCell = document.createElement("td");
//           valCell.textContent = Array.isArray(v) ? v.join(", ") : v;

//           row.appendChild(keyCell);
//           row.appendChild(valCell);
//           table.appendChild(row);
//         }
//         content.appendChild(table);
//       } else {
//         content.textContent = value;
//       }

//       section.appendChild(title);
//       section.appendChild(content);
//       container.appendChild(section);
//     });

//     addFooter();
//   }

//   function formatKey(k) {
//     return k
//       .replace(/_/g, " ")
//       .replace(/^\w/, (c) => c.toUpperCase());
//   }

//   function addFooter() {
//     const oldFooter = document.querySelector(".footer");
//     if (oldFooter) oldFooter.remove();

//     const footer = document.createElement("footer");
//     footer.className = "footer text-center text-light py-3 mt-5";
//     footer.innerHTML = `
//       <div class="container">
//         <small>&copy; ${new Date().getFullYear()} Turbify SEO Analyzer. All rights reserved.</small>
//       </div>
//     `;
//     document.body.appendChild(footer);
//   }

//   // Scroll to section if sidebar link is clicked
//   document.querySelectorAll("#sidebar-sections a").forEach((link) => {
//     link.addEventListener("click", function (e) {
//       e.preventDefault();
//       const target = document.querySelector(this.getAttribute("href"));
//       if (target) {
//         target.scrollIntoView({ behavior: "smooth", block: "start" });
//       }
//     });
//   });
// });


document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("search-form");
  const input = document.getElementById("search-input");
  const scoreBox = document.getElementById("seo-score");
  const container = document.getElementById("details-container");
  const loaderContainer = document.getElementById("loader-container");

  form.addEventListener("submit", async function (e) {
    e.preventDefault();
    const url = input.value.trim();
    if (!url) return;

    try {
      loaderContainer.style.display = "block";
      scoreBox.textContent = "";
      scoreBox.classList.add("loading");
      container.innerHTML = "";

      const response = await fetch(
        `http://localhost:9090/api/seo/analyze?url=${encodeURIComponent(url)}`
      );

      loaderContainer.style.display = "none";

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const data = await response.json();
      scoreBox.textContent = data["SEO Score"] + "/100";
      scoreBox.classList.remove("loading");
      renderDetails(data["seo_analysis"]);
    } catch (err) {
      loaderContainer.style.display = "none";
      console.error("Fetch error:", err);
      scoreBox.textContent = "Error!";
      scoreBox.classList.remove("loading");
      alert("Failed to fetch SEO data. See console for details.");
    }
  });

  function renderDetails(analysis) {
    Object.entries(analysis).forEach(([key, value]) => {
      const section = document.createElement("div");
      section.className = "section-content";
      section.id = key;

      const title = document.createElement("div");
      title.className = "section-title";
      title.textContent = key
        .replace(/_/g, " ")
        .split(" ")
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(" ");

      const content = document.createElement("div");
      content.className = "section-data";

      if (typeof value === "object" && !Array.isArray(value)) {
        const table = document.createElement("table");
        for (const [k, v] of Object.entries(value)) {
          const row = document.createElement("tr");
          const keyCell = document.createElement("td");
          keyCell.textContent = k;
          const valCell = document.createElement("td");
          valCell.textContent = Array.isArray(v) ? v.join(", ") : v;
          row.appendChild(keyCell);
          row.appendChild(valCell);
          table.appendChild(row);
        }
        content.appendChild(table);
      } else {
        content.textContent = value;
      }

      section.appendChild(title);
      section.appendChild(content);
      container.appendChild(section);
    });

    addFooter();
  }


});
