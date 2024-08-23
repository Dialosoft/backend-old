package request

type CreateForum struct {
	Name        string `json:"forumName"`
	Description string `json:"description"`
	Type        string `json:"type"`
	CategoryID  string `json:"categoryId"`
}
