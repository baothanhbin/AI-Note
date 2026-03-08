# AI Note / Knowledge Vault - Master Specification & Roadmap

Tài liệu này định nghĩa chi tiết kế hoạch nâng cấp Giao diện (UI/UX) và tích hợp Trí tuệ Nhân tạo (AI Features) cho dự án AI Note, hướng tới mục tiêu xây dựng một "Minimal Knowledge Workspace" cấp độ cao.

---

## 1. Tầm nhìn & Design Direction

**Mục tiêu chính:** Biến app từ một ứng dụng ghi chú cơ bản thành một sản phẩm có trải nghiệm hiện đại, tối giản nhưng cao cấp, tối ưu cho việc đọc, viết, liên kết tri thức và có chiều sâu thị giác.

**Từ khóa cốt lõi:** `Calm` - `Focused` - `Elegant` - `Intelligent` - `Reflective` - `Connected`.

**Nguồn cảm hứng:** Obsidian, Notion, Reflect, Capacities.

**Visual Style:**
*   **Nền sáng:** Off-white hoặc warm gray rất nhẹ.
*   **Nền tối:** Charcoal / deep slate thay vì đen tuyệt đối.
*   **Card & Shape:** Bo góc lớn (16dp - 24dp), card nổi nhẹ bằng tonal elevation, tạo nhiều khoảng thở (spacing rộng).
*   **Details:** Icon nét mảnh đồng bộ, typography ưu tiên phân cấp rõ ràng và dễ đọc.

---

## 2. Design System Updates (core:designsystem)

Hệ thống Design System cần được rà soát và mở rộng để đáp ứng nhu cầu UX mới:

### 2.1. Color Tokens Mở Rộng
*   **Surfaces:** `surfacePrimary`, `surfaceSecondary`, `surfaceElevated`, `surfaceMuted`.
*   **Borders:** `borderSubtle`, `borderStrong`.
*   **Accents:** `accentBlue`, `accentPurple`, `accentGreen`.
*   **States:** `warningSoft`, `successSoft`.
*   **Semantic Colors:**
    *   `notePinned`, `noteLinked`.
    *   `tagBackground`, `tagText`.
    *   `graphNode`, `graphEdge`, `graphNodeActive`.
    *   `searchHighlight`.

### 2.2. Typography Scale
Sử dụng font chữ hiện đại, rõ nét (như Inter, Roboto hoặc Outfit):
*   `Display`, `Title Large`, `Title Medium`.
*   `Body Large`, `Body Medium`.
*   `Label Large`, `Label Small`.
*   `Code Text`.

### 2.3. Shape & Motion System
*   **Shapes:** `Small` (12dp), `Medium` (16dp), `Large` (20dp), `Extra Large` (28dp).
*   **Motion:** Fade + slide nhẹ khi mở màn hình, card press animation mượt mà, crossfade khi đổi layout, animated content/graph highlights.

---

## 3. Information Architecture (IA) Mới

Cấu trúc ứng dụng sẽ được chia thành 5 vùng chính, tổ chức qua Bottom Navigation hoặc Navigation Rail:
1.  **Home:** Bảng điều khiển cá nhân (Dashboard).
2.  **Search:** Tìm kiếm thông minh qua semantic/hybrid search.
3.  **Graph:** Khám phá liên kết tri thức trực quan.
4.  **Tags (Hub):** Quản lý và phân loại theo chủ đề.
5.  **Settings:** Cài đặt ứng dụng.

*(Lưu ý: Editor/Detail sẽ được mở dưới dạng một luồng/screen riêng biệt đè lên navigation chính để tối đa sự tập trung)*.

---

## 4. Đặc Tả Màn Hình Chi Tiết (Screen Specifications)

### 4.1. Home Screen (Dashboard)
*   **Header:** Large Collapsible Header hiển thị trạng thái tổng quan (số ghi chú đã tạo hôm nay, số ghim...). Có các nút tắt: Dearch, Graph, AI Quick Actions.
*   **Quick Insights & Actions:** Các card ngang báo cáo (Tổng notes, tags, link, ghi chú chỉnh sửa gần nhất), hàng action chips (New Note, Ask AI...).
*   **Smart Search Bar:** Thanh tìm kiếm đa nhiệm, placeholder thông minh, kèm query recent.
*   **Sections Phân Loại:** Continue Thinking, Pinned, Recent Notes, Knowledge Clusters.
*   **Chế độ xem & Bộ lọc:** Comfortable List, Compact List, Card Grid. Bottom sheet thông minh cho Sort/Filter. Tương tác long-press đa năng.

### 4.2. Editor / Detail Screen
*   **Layout:** Top bar tối giản (trạng thái, ghim, menu). Vùng soạn thảo rộng, font chữ thoáng, markdown syntax + code block styled riêng.
*   **Floating Contextual Toolbar:** Chứa công cụ (Markdown, Checklist, Code, Tag, Link, AI, Preview) có thể cuộn nổi dưới màn hình.
*   **Bottom Sheet (Side Info):** Quản lý Tags, Linked notes, Backlinks, Metadata, và AI Suggestions.
*   **AI Assist Zone:** Nút "AI Assist" bật Sheet hỗ trợ (tóm tắt, cải thiện câu văn, trích xuất task...).
*   **Rich Interactions:** Lệnh gõ tắt bằng `/` (slash command), autocomplete cho gõ kép `[[` để link, autosave, preview mode (Edit/Split/Preview).

### 4.3. Search Screen
*   **Giao Diện Phẳng:** Full screen search, top bar lớn, list các filter chips.
*   **Smart Search:** Hỗ trợ Natural Language Search, AI Semantic Search cục bộ, Query expansion do AI gợi ý.
*   **Kết Quả Hiển Thị:** Từng match card chứa snippet (highlight đỏ/vàng tuỳ theme), tag chips, icon loại file và metadata, cùng "Relevance score".

### 4.4. Knowledge Graph
*   **UI Định Hướng:** Nền tối nhẹ giúp node nổi bật (glowing), selected edges / ring animation rõ ràng. Cung cấp Map/Zoom/Pan mượt.
*   **Khám Phá:** Node đổi màu theo tag cluster, kích thước to nhỏ dự trên node degree. Bấm vào node sẽ show Mini Preview sheet.
*   **Graph AI:** Auto-detect clusters (cụm ý tưởng), gợi ý "Cụm tri thức chính", phát hiện orphan notes, timeline liên kết.

### 4.5. Tags / Collections
*   **Hub Layout:** Most used, Recently used, All tags, Untagged. Khi click vào tag sẽ thấy Stats (số lượng notes, số links) & Mini Graph riêng.
*   **Tag AI:** Đề xuất gộp tags tương tự, suggest missing tags.

---

## 5. Danh Sách AI Features (Chức Năng Thông Minh)

1.  **AI Writing Assistant:** Tự tạo title, tóm tắt nội dung, cấu trúc lại câu (rewrite: concise, technical), trích xuất Action Items (Checklist).
2.  **AI Knowledge Linking:** Gợi ý note liên quan ngay khi gõ, phát hiện orphan note và gợi ý tự động tạo link (You should connect...).
3.  **AI Auto Tagging:** Nhận diện và tự gán tag, đề xuất chuẩn hóa hoặc gộp (merge) tags trùng nghĩa.
4.  **AI Semantic Search:** Tìm hiểu từ đồng nghĩa hoặc câu hỏi tự nhiên thay vì full-text-search thông thường.
5.  **AI Daily/Weekly Insights:** Recap kiến thức vừa học, highlights cụm ý tưởng đã phát triển trong tuần.
6.  **AI Note Chat (Ask Your Notes):** Cho phép đặt câu hỏi với context là toàn bộ Knowledge Vault. Trả lời kèm trích dẫn và link tới note gốc.
7.  **AI Knowledge Health:** Chấm điểm cấu trúc kho tàng (phát hiện note rác, tags rỗng...).

---

## 6. Architecture Updates (Module Bổ Sung)

Cần bổ sung các data sources và repository để wrap Logic AI giúp mở rộng sau này (ví dụ đổi AI provider từ Local LLM sang Gemini/Vertex...):

**Package/Module Đề Xuất:**
```
+ com.myapp.data.ai
    + provider (GeminiProvider, LocalLlmProvider)
    + mapper
+ com.myapp.data.repository
    + AiAssistRepository (Writing, Summarization)
    + SemanticSearchRepository (Vector embeddings, Hybrid search)
    + KnowledgeInsightRepository (Graph AI logic, Analytics)
```

(Trong giai đoạn đầu, có thể không tách hẳn `feature:ai` mà gắn UI Panels của AI vào thẳng `feature:editor` hoặc `feature:home` để MVP nhanh).

---

## 7. Roadmap UI + AI Thực Tế (Phased Implementation)

Lộ trình được thiết kế đi từ nền tảng vững chắc tới chức năng đột phá, chia làm 4 bước.

### Giai Đoạn 1: Nền tảng UI/UX & Foundation (Tuần 1-2)
*   **Mục tiêu:** App trông hiện đại và chuyên nghiệp ngay lập tức.
*   **Công việc:**
    *   Nâng cấp `core:designsystem` (Màu sắc mới, Surface Elevation, Typography chuẩn).
    *   Tái cấu trúc Note Card, thiết kế hệ thống Skeleton / Empty States.
    *   Xây dựng UI cho Home Dashboard (Header collapsible, Quick insight row).
    *   Thiết kế Editor Component (Clean background, Floating toolbar, / command cơ bản).
    *   Refactor Navigation Layout.

### Giai Đoạn 2: AI Foundation + Workflow Mượt (Tuần 3-4)
*   **Mục tiêu:** Biến AI thành một công cụ soạn thảo đắc lực.
*   **Công việc:**
    *   Tích hợp Provider AI (Gemini SDK / OpenAI API/ Cục bộ).
    *   Xây dựng AI Writing Assistant trong Editor (Summarize, Đề xuất Title, Extract checklist).
    *   Phát triển Auto Tagging AI & Đề xuất (Suggest missing tags).
    *   Cải tiến Split view/Preview Markdown.

### Giai Đoạn 3: Knowledge Graph & Trực Quan Hoá (Tuần 5-6)
*   **Mục tiêu:** Đặc tả sức mạnh liên kết tri thức (Signature Feature).
*   **Công việc:**
    *   Xây dựng/Nâng cấp Graph UI Canvas (Zoom, Pan, Clustering colorization).
    *   Xây dựng Bottom sheet cho Node preview.
    *   Tích hợp bộ lọc trên Graph.
    *   Xây dựng AI Suggest Related Notes ngay bên trong editor (phát hiện keyword và backlink potential).

### Giai Đoạn 4: Trí Tuệ Nâng Cao (Smart Search & Ask AI) (Tuần 7-8)
*   **Mục tiêu:** Trở thành vault tri thức thực thụ (Second Brain).
*   **Công việc:**
    *   Phát triển Hybrid Search / Semantic Search (Kết hợp Full-Text và Query semantic expansion).
    *   Tính năng: **Ask Your Notes** (RAG cục bộ / Trích xuất context từ Database Room/FTS đưa vào prompt).
    *   Xây dựng Dashboard API Insights & Knowledge Health Assessment (Kiểm tra liên kết nghèo nàn).
