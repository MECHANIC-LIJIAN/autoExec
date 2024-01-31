class UserTaskModel extends RectNodeModel { 

    initNodeData(data) {
        super.initNodeData(data);
    
        const rule = {
          message: "只允许从右边的锚点连出",
          validate: (sourceNode, targetNode, sourceAnchor, targetAnchor) => {
            console.log(sourceNode)
            return sourceAnchor.name === "bottom";
          },
        };
        this.sourceRules.push(rule);
      }
}

class UserTaskView extends RectNode { }

export default {
  type: "UserTask",
  view: UserTaskView,
  model: UserTaskModel,
};