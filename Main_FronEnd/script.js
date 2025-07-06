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

      renderDetails(data["seo_analysis"]);
    } catch (err) {
      loaderContainer.style.display = "none";
      console.error("Fetch error:", err);
      scoreBox.textContent = "Error!";
      alert("Failed to fetch SEO data. Check console for more info.");
    }
  });

  function renderDetails(analysis) {
    container.innerHTML = ""; // clear old content
    Object.entries(analysis).forEach(([key, value]) => {
      const wrapper = document.createElement("div");
      wrapper.className = "section col-12";

      const titleBlock = document.createElement("div");
      titleBlock.className = "section-title-block";
      titleBlock.textContent = key.replace(/_/g, " ");

      const dataBlock = document.createElement("div");
      dataBlock.className = "section-data-block";
      dataBlock.textContent = JSON.stringify(value, null, 2);

      wrapper.appendChild(titleBlock);
      wrapper.appendChild(dataBlock);
      container.appendChild(wrapper);
    });
  }
});
