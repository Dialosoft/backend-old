package request

type ForumUpdateCategoryOwner struct {
	ForumID    string `json:"forumId"`
	CategoryID string `json:"categoryId"`
}

type ForumUpdateDescription struct {
	ForumID     string `json:"forumId"`
	Description string `json:"description"`
}

type ForumUpdateName struct {
	ForumID string `json:"forumId"`
	Name    string `json:"name"`
}
